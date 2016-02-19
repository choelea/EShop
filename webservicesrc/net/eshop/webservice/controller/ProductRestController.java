/*
 *
 *
 *
 */
package net.eshop.webservice.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;

import net.eshop.FileInfo.FileType;
import net.eshop.Message;
import net.eshop.Pageable;
import net.eshop.controller.admin.AbstractProductController;
import net.eshop.entity.Attribute;
import net.eshop.entity.Brand;
import net.eshop.entity.Goods;
import net.eshop.entity.MemberRank;
import net.eshop.entity.Parameter;
import net.eshop.entity.ParameterGroup;
import net.eshop.entity.Product;
import net.eshop.entity.Product.OrderType;
import net.eshop.entity.ProductCategory;
import net.eshop.entity.ProductImage;
import net.eshop.entity.Promotion;
import net.eshop.entity.Specification;
import net.eshop.entity.SpecificationValue;
import net.eshop.entity.Tag;
import net.eshop.entity.Tag.Type;
import net.eshop.service.BrandService;
import net.eshop.service.FileService;
import net.eshop.service.GoodsService;
import net.eshop.service.MemberRankService;
import net.eshop.service.ProductCategoryService;
import net.eshop.service.ProductImageService;
import net.eshop.service.ProductService;
import net.eshop.service.PromotionService;
import net.eshop.service.SpecificationService;
import net.eshop.service.SpecificationValueService;
import net.eshop.service.TagService;
import net.eshop.util.JsonUtils;
import net.eshop.webservice.response.Response;
import net.eshop.webservice.util.ResponseUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Controller - 商品
 *
 *
 *
 */
@Controller
public class ProductRestController extends AbstractProductController
{

	private static Logger LOG = Logger.getLogger(ProductRestController.class);

	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;
	@Resource(name = "brandServiceImpl")
	private BrandService brandService;
	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;
	@Resource(name = "tagServiceImpl")
	private TagService tagService;
	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;
	@Resource(name = "productImageServiceImpl")
	private ProductImageService productImageService;
	@Resource(name = "specificationServiceImpl")
	private SpecificationService specificationService;
	@Resource(name = "specificationValueServiceImpl")
	private SpecificationValueService specificationValueService;
	@Resource(name = "fileServiceImpl")
	private FileService fileService;

	/**
	 * 检查编号是否唯一
	 */
	@RequestMapping(value = "/check_sn", method = RequestMethod.GET)
	public @ResponseBody boolean checkSn(final String previousSn, final String sn)
	{
		if (StringUtils.isEmpty(sn))
		{
			return false;
		}
		if (productService.snUnique(previousSn, sn))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/employee")
	public ModelAndView addEmployee(@RequestBody final String body)
	{
		System.out.println("haha");

		return new ModelAndView("");
	}

	@RequestMapping(value = "/rest/product/{id}", method = RequestMethod.GET)
	public ResponseEntity<Product> getProduct(@PathVariable("id") final long id, final HttpServletRequest request)
	{
		System.out.println("Fetching User with id " + id);
		final Product product = productService.find(id);
		if (product == null)
		{
			System.out.println("Product with id " + id + " not found");
			return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Product>(product, HttpStatus.OK);
	}

	@RequestMapping(value = "/rest/product", method = RequestMethod.POST)
	@ResponseBody
	public Response createProduct(@RequestBody final String productStr)
	{
		final List<ProductCategory> categories = productCategoryService.findRoots();
		Response response = null;
		final Product product = JsonUtils.toObject(productStr, Product.class);
		product.setIsList(Boolean.FALSE);
		product.setIsMarketable(Boolean.FALSE);
		product.setIsTop(Boolean.FALSE);
		product.setProductCategory(categories.get(0));
		product.setPoint(calculateDefaultPoint(product.getPrice()));
		if (!isValid(product))
		{
			final StringBuffer sb = new StringBuffer();
			final RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
			final Set<ConstraintViolation<Product>> constraintViolations = (Set<ConstraintViolation<Product>>) requestAttributes
					.getAttribute(CONSTRAINT_VIOLATIONS_ATTRIBUTE_NAME, RequestAttributes.SCOPE_REQUEST);
			for (final ConstraintViolation<Product> constraintViolation : constraintViolations)
			{
				sb.append(constraintViolation.getPropertyPath() + constraintViolation.getMessage() + ";");
			}
			response = ResponseUtil.createErrorResponse(sb.toString());
		}
		else
		{
			if (product.getMarketPrice() == null)
			{
				final BigDecimal defaultMarketPrice = calculateDefaultMarketPrice(product.getPrice());
				product.setMarketPrice(defaultMarketPrice);
			}

			product.setFullName(null);
			product.setAllocatedStock(0);
			product.setScore(0F);
			product.setTotalScore(0L);
			product.setScoreCount(0L);
			product.setHits(0L);
			product.setWeekHits(0L);
			product.setMonthHits(0L);
			product.setSales(0L);
			product.setWeekSales(0L);
			product.setMonthSales(0L);
			product.setWeekHitsDate(new Date());
			product.setMonthHitsDate(new Date());
			product.setWeekSalesDate(new Date());
			product.setMonthSalesDate(new Date());
			product.setReviews(null);
			product.setConsultations(null);
			product.setFavoriteMembers(null);
			product.setPromotions(null);
			product.setCartItems(null);
			product.setOrderItems(null);
			product.setGiftItems(null);
			product.setProductNotifies(null);


			final Goods goods = new Goods();
			final List<Product> products = new ArrayList<Product>();
			product.setGoods(goods);
			product.setSpecifications(null);
			product.setSpecificationValues(null);
			products.add(product);
			goods.getProducts().clear();
			goods.getProducts().addAll(products);
			try
			{
				goodsService.save(goods);
				response = ResponseUtil.createResponse();
			}
			catch (final Exception e)
			{
				LOG.error(e.getMessage());
				response = ResponseUtil.createErrorResponse(e.getMessage());
			}

		}
		return response;
	}

	/**
	 * 获取参数组
	 */
	@RequestMapping(value = "/parameter_groups", method = RequestMethod.GET)
	public @ResponseBody Set<ParameterGroup> parameterGroups(final Long id)
	{
		final ProductCategory productCategory = productCategoryService.find(id);
		return productCategory.getParameterGroups();
	}

	/**
	 * 获取属性
	 */
	@RequestMapping(value = "/attributes", method = RequestMethod.GET)
	public @ResponseBody Set<Attribute> attributes(final Long id)
	{
		final ProductCategory productCategory = productCategoryService.find(id);
		return productCategory.getAttributes();
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(final ModelMap model)
	{
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("tags", tagService.findList(Type.product));
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("specifications", specificationService.findAll());
		return "/admin/product/add";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(final Long id, final ModelMap model)
	{
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("tags", tagService.findList(Type.product));
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("specifications", specificationService.findAll());
		model.addAttribute("product", productService.find(id));
		return "/admin/product/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(final Product product, final Long productCategoryId, final Long brandId, final Long[] tagIds,
			final Long[] specificationIds, final Long[] specificationProductIds, final HttpServletRequest request,
			final RedirectAttributes redirectAttributes)
	{
		for (final Iterator<ProductImage> iterator = product.getProductImages().iterator(); iterator.hasNext();)
		{
			final ProductImage productImage = iterator.next();
			if (productImage == null || productImage.isEmpty())
			{
				iterator.remove();
				continue;
			}
			if (productImage.getFile() != null && !productImage.getFile().isEmpty())
			{
				if (!fileService.isValid(FileType.image, productImage.getFile()))
				{
					addFlashMessage(redirectAttributes, Message.error("admin.upload.invalid"));
					return "redirect:edit.jhtml?id=" + product.getId();
				}
			}
		}
		product.setProductCategory(productCategoryService.find(productCategoryId));
		product.setBrand(brandService.find(brandId));
		product.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
		if (!isValid(product))
		{
			return ERROR_VIEW;
		}
		final Product pProduct = productService.find(product.getId());
		if (pProduct == null)
		{
			return ERROR_VIEW;
		}
		if (StringUtils.isNotEmpty(product.getSn()) && !productService.snUnique(pProduct.getSn(), product.getSn()))
		{
			return ERROR_VIEW;
		}
		if (product.getMarketPrice() == null)
		{
			final BigDecimal defaultMarketPrice = calculateDefaultMarketPrice(product.getPrice());
			product.setMarketPrice(defaultMarketPrice);
		}
		if (product.getPoint() == null)
		{
			final long point = calculateDefaultPoint(product.getPrice());
			product.setPoint(point);
		}

		for (final MemberRank memberRank : memberRankService.findAll())
		{
			final String price = request.getParameter("memberPrice_" + memberRank.getId());
			if (StringUtils.isNotEmpty(price) && new BigDecimal(price).compareTo(new BigDecimal(0)) >= 0)
			{
				product.getMemberPrice().put(memberRank, new BigDecimal(price));
			}
			else
			{
				product.getMemberPrice().remove(memberRank);
			}
		}

		for (final ProductImage productImage : product.getProductImages())
		{
			productImageService.build(productImage);
		}
		Collections.sort(product.getProductImages());
		if (product.getImage() == null && product.getThumbnail() != null)
		{
			product.setImage(product.getThumbnail());
		}

		for (final ParameterGroup parameterGroup : product.getProductCategory().getParameterGroups())
		{
			for (final Parameter parameter : parameterGroup.getParameters())
			{
				final String parameterValue = request.getParameter("parameter_" + parameter.getId());
				if (StringUtils.isNotEmpty(parameterValue))
				{
					product.getParameterValue().put(parameter, parameterValue);
				}
				else
				{
					product.getParameterValue().remove(parameter);
				}
			}
		}

		for (final Attribute attribute : product.getProductCategory().getAttributes())
		{
			final String attributeValue = request.getParameter("attribute_" + attribute.getId());
			if (StringUtils.isNotEmpty(attributeValue))
			{
				product.setAttributeValue(attribute, attributeValue);
			}
			else
			{
				product.setAttributeValue(attribute, null);
			}
		}

		final Goods goods = pProduct.getGoods();
		final List<Product> products = new ArrayList<Product>();
		if (specificationIds != null && specificationIds.length > 0)
		{
			for (int i = 0; i < specificationIds.length; i++)
			{
				final Specification specification = specificationService.find(specificationIds[i]);
				final String[] specificationValueIds = request.getParameterValues("specification_" + specification.getId());
				if (specificationValueIds != null && specificationValueIds.length > 0)
				{
					for (int j = 0; j < specificationValueIds.length; j++)
					{
						if (i == 0)
						{
							if (j == 0)
							{
								BeanUtils.copyProperties(product, pProduct, new String[]
								{ "id", "createDate", "modifyDate", "fullName", "allocatedStock", "score", "totalScore", "scoreCount",
										"hits", "weekHits", "monthHits", "sales", "weekSales", "monthSales", "weekHitsDate",
										"monthHitsDate", "weekSalesDate", "monthSalesDate", "goods", "reviews", "consultations",
										"favoriteMembers", "specifications", "specificationValues", "promotions", "cartItems",
										"orderItems", "giftItems", "productNotifies" });
								pProduct.setSpecifications(new HashSet<Specification>());
								pProduct.setSpecificationValues(new HashSet<SpecificationValue>());
								products.add(pProduct);
							}
							else
							{
								if (specificationProductIds != null && j < specificationProductIds.length)
								{
									final Product specificationProduct = productService.find(specificationProductIds[j]);
									if (specificationProduct == null
											|| (specificationProduct.getGoods() != null && !specificationProduct.getGoods().equals(goods)))
									{
										return ERROR_VIEW;
									}
									specificationProduct.setSpecifications(new HashSet<Specification>());
									specificationProduct.setSpecificationValues(new HashSet<SpecificationValue>());
									products.add(specificationProduct);
								}
								else
								{
									final Product specificationProduct = new Product();
									BeanUtils.copyProperties(product, specificationProduct);
									specificationProduct.setId(null);
									specificationProduct.setCreateDate(null);
									specificationProduct.setModifyDate(null);
									specificationProduct.setSn(null);
									specificationProduct.setFullName(null);
									specificationProduct.setAllocatedStock(0);
									specificationProduct.setIsList(false);
									specificationProduct.setScore(0F);
									specificationProduct.setTotalScore(0L);
									specificationProduct.setScoreCount(0L);
									specificationProduct.setHits(0L);
									specificationProduct.setWeekHits(0L);
									specificationProduct.setMonthHits(0L);
									specificationProduct.setSales(0L);
									specificationProduct.setWeekSales(0L);
									specificationProduct.setMonthSales(0L);
									specificationProduct.setWeekHitsDate(new Date());
									specificationProduct.setMonthHitsDate(new Date());
									specificationProduct.setWeekSalesDate(new Date());
									specificationProduct.setMonthSalesDate(new Date());
									specificationProduct.setGoods(goods);
									specificationProduct.setReviews(null);
									specificationProduct.setConsultations(null);
									specificationProduct.setFavoriteMembers(null);
									specificationProduct.setSpecifications(new HashSet<Specification>());
									specificationProduct.setSpecificationValues(new HashSet<SpecificationValue>());
									specificationProduct.setPromotions(null);
									specificationProduct.setCartItems(null);
									specificationProduct.setOrderItems(null);
									specificationProduct.setGiftItems(null);
									specificationProduct.setProductNotifies(null);
									products.add(specificationProduct);
								}
							}
						}
						final Product specificationProduct = products.get(j);
						final SpecificationValue specificationValue = specificationValueService.find(Long
								.valueOf(specificationValueIds[j]));
						specificationProduct.getSpecifications().add(specification);
						specificationProduct.getSpecificationValues().add(specificationValue);
					}
				}
			}
		}
		else
		{
			product.setSpecifications(null);
			product.setSpecificationValues(null);
			BeanUtils.copyProperties(product, pProduct, new String[]
			{ "id", "createDate", "modifyDate", "fullName", "allocatedStock", "score", "totalScore", "scoreCount", "hits",
					"weekHits", "monthHits", "sales", "weekSales", "monthSales", "weekHitsDate", "monthHitsDate", "weekSalesDate",
					"monthSalesDate", "goods", "reviews", "consultations", "favoriteMembers", "promotions", "cartItems", "orderItems",
					"giftItems", "productNotifies" });
			products.add(pProduct);
		}
		goods.getProducts().clear();
		goods.getProducts().addAll(products);
		goodsService.update(goods);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(final Long productCategoryId, final Long brandId, final Long promotionId, final Long tagId,
			final Boolean isMarketable, final Boolean isList, final Boolean isTop, final Boolean isGift, final Boolean isOutOfStock,
			final Boolean isStockAlert, final Pageable pageable, final ModelMap model)
	{
		final ProductCategory productCategory = productCategoryService.find(productCategoryId);
		final Brand brand = brandService.find(brandId);
		final Promotion promotion = promotionService.find(promotionId);
		final List<Tag> tags = tagService.findList(tagId);
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("promotions", promotionService.findAll());
		model.addAttribute("tags", tagService.findList(Type.product));
		model.addAttribute("productCategoryId", productCategoryId);
		model.addAttribute("brandId", brandId);
		model.addAttribute("promotionId", promotionId);
		model.addAttribute("tagId", tagId);
		model.addAttribute("isMarketable", isMarketable);
		model.addAttribute("isList", isList);
		model.addAttribute("isTop", isTop);
		model.addAttribute("isGift", isGift);
		model.addAttribute("isOutOfStock", isOutOfStock);
		model.addAttribute("isStockAlert", isStockAlert);
		model.addAttribute("page", productService.findPage(productCategory, brand, promotion, tags, null, null, null, isMarketable,
				isList, isTop, isGift, isOutOfStock, isStockAlert, OrderType.dateDesc, pageable));
		return "/admin/product/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Message delete(final Long[] ids)
	{
		productService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}